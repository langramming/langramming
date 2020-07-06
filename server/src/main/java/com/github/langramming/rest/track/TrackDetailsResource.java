package com.github.langramming.rest.track;

import com.github.langramming.model.TrackProviderType;
import com.github.langramming.model.TrackDetails;
import com.github.langramming.rest.response.TrackDTO;
import com.github.langramming.service.TrackService;
import com.github.langramming.util.ResponseHelper;
import io.atlassian.fugue.Option;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/api/track/details")
public class TrackDetailsResource {

    private final TrackService trackService;
    private final ResponseHelper responseHelper;

    @Inject
    public TrackDetailsResource(
            TrackService trackService,
            ResponseHelper responseHelper) {
        this.trackService = trackService;
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

        Optional<TrackDetails> trackDetailsOpt = trackService.getTrackDetails(trackProviderType, trackId);

        return Option.fromOptional(trackDetailsOpt)
                .map(TrackDTO::new)
                .fold(responseHelper::notFound, responseHelper::ok);
    }
}
