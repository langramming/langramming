package dev.nickrobson.langramming.rest.track;

import static java.util.Collections.emptyList;

import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import dev.nickrobson.langramming.client.spotify.SpotifyRestClient;
import dev.nickrobson.langramming.manager.TrackManager;
import dev.nickrobson.langramming.model.TrackDetails;
import dev.nickrobson.langramming.model.TrackProviderType;
import dev.nickrobson.langramming.rest.response.PageInfoDTO;
import dev.nickrobson.langramming.rest.response.RecentSpotifyTrackDTO;
import dev.nickrobson.langramming.service.SpotifyUserService;
import dev.nickrobson.langramming.util.ResponseHelper;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/track/recent")
public class RecentTracksResource {

    private final ResponseHelper responseHelper;
    private final TrackManager trackManager;
    private final SpotifyUserService spotifyUserService;
    private final SpotifyRestClient spotifyRestClient;

    @Inject
    public RecentTracksResource(
        ResponseHelper responseHelper,
        TrackManager trackManager,
        SpotifyUserService spotifyUserService,
        SpotifyRestClient spotifyRestClient
    ) {
        this.responseHelper = responseHelper;
        this.trackManager = trackManager;
        this.spotifyUserService = spotifyUserService;
        this.spotifyRestClient = spotifyRestClient;
    }

    @GetMapping(path = "/spotify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRecentSpotifyTracks(
        @RequestParam(name = "before", required = false) Instant before,
        @RequestParam(name = "after", required = false) Instant after,
        @RequestParam(name = "limit", defaultValue = "50") int limit
    ) {
        if (spotifyUserService.getCurrentSpotifyUser().isEmpty()) {
            return responseHelper.unauthorized();
        }

        GetCurrentUsersRecentlyPlayedTracksRequest.Builder requestBuilder = spotifyRestClient
            .getAuthenticatedSpotifyApi()
            .getCurrentUsersRecentlyPlayedTracks()
            .limit(limit);

        addParameter("before", before, requestBuilder);
        addParameter("after", after, requestBuilder);

        Either<Exception, PageInfoDTO<RecentSpotifyTrackDTO>> recentTracksEither = fetchRecentSpotifyTracks(
            requestBuilder.build()
        );
        return responseHelper.fromEither(recentTracksEither);
    }

    private void addParameter(
        @Nonnull String parameter,
        @Nullable Instant instant,
        @Nonnull GetCurrentUsersRecentlyPlayedTracksRequest.Builder builder
    ) {
        Optional
            .ofNullable(instant)
            .map(Instant::toEpochMilli)
            .ifPresent(ms -> builder.setQueryParameter(parameter, ms));
    }

    private Either<Exception, PageInfoDTO<RecentSpotifyTrackDTO>> fetchRecentSpotifyTracks(
        GetCurrentUsersRecentlyPlayedTracksRequest request
    ) {
        return Checked
            .now(request::execute)
            .toEither()
            .map(
                response -> {
                    Map<String, PlayHistory> itemsByTrackId = Arrays
                        .stream(response.getItems())
                        .collect(
                            Collectors.toMap(
                                item -> item.getTrack().getId(),
                                Function.identity(),
                                (first, second) -> first, // ignore duplicates
                                LinkedHashMap::new
                            )
                        );

                    int total = Optional
                        .ofNullable(response.getTotal())
                        .orElseGet(response::getLimit);

                    if (itemsByTrackId.isEmpty()) {
                        return PageInfoDTO
                            .<RecentSpotifyTrackDTO>builder()
                            .total(total)
                            .items(emptyList())
                            .build();
                    }

                    List<Pair<PlayHistory, TrackDetails>> tracks = itemsByTrackId
                        .entrySet()
                        .stream()
                        .map(
                            entry ->
                                trackManager
                                    .getTrackDetails(TrackProviderType.SPOTIFY, entry.getKey())
                                    .map(trackDetails -> Pair.pair(entry.getValue(), trackDetails))
                        )
                        .flatMap(Optional::stream)
                        .collect(Collectors.toList());

                    Optional<PlayHistory> latest = Optional.empty();
                    Optional<PlayHistory> earliest = Optional.empty();
                    List<PlayHistory> playHistoryItems = Arrays
                        .stream(response.getItems())
                        .sorted(Comparator.comparing(PlayHistory::getPlayedAt))
                        .toList();
                    if (!playHistoryItems.isEmpty()) {
                        latest = Optional.of(playHistoryItems.get(0));
                        earliest = Optional.of(playHistoryItems.get(playHistoryItems.size() - 1));
                    }

                    Function<PlayHistory, String> toDateString = ph ->
                        String.valueOf(ph.getPlayedAt().getTime());

                    return PageInfoDTO
                        .<RecentSpotifyTrackDTO>builder()
                        .first(latest.map(toDateString))
                        .last(earliest.map(toDateString))
                        .total(total)
                        .items(
                            tracks
                                .stream()
                                .map(
                                    track ->
                                        new RecentSpotifyTrackDTO(
                                            track.right(),
                                            track.left().getPlayedAt(),
                                            track.left().getTrack().getPreviewUrl()
                                        )
                                )
                                .collect(Collectors.toList())
                        )
                        .build();
                }
            );
    }
}
