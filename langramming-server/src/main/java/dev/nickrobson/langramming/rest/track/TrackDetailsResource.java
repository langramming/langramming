package dev.nickrobson.langramming.rest.track;

import dev.nickrobson.langramming.manager.TrackManager;
import dev.nickrobson.langramming.model.TrackDetails;
import dev.nickrobson.langramming.model.TrackProviderType;
import dev.nickrobson.langramming.rest.response.TrackDTO;
import dev.nickrobson.langramming.util.ResponseHelper;
import io.atlassian.fugue.Option;
import java.util.Optional;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/track/details")
public class TrackDetailsResource {

    private final TrackManager trackManager;
    private final ResponseHelper responseHelper;

    @Inject
    public TrackDetailsResource(TrackManager trackManager, ResponseHelper responseHelper) {
        this.trackManager = trackManager;
        this.responseHelper = responseHelper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTrackDetails(
        @RequestParam("provider") @NotNull TrackProviderType trackProviderType,
        @RequestParam("id") @NotNull String trackId
    ) {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        Optional<TrackDetails> trackDetailsOpt = trackManager.getTrackDetails(
            trackProviderType,
            trackId
        );

        return Option
            .fromOptional(trackDetailsOpt)
            .map(TrackDTO::new)
            .fold(responseHelper::notFound, responseHelper::ok);
    }
}
