import * as React from 'react';
import styled from 'styled-components';
import { Card } from 'evergreen-ui';
import { Track } from '../../types/Track';
import { AudioPlayer } from '../AudioPlayer';

interface SpotifyTrackProps {
  track: Track;
  previewUrl: string;
}

const TrackContainer = styled.div`
  display: flex;
  flex-direction: row;
  max-height: 192px;

  > img {
    max-height: inherit;
    max-width: auto;
  }
`;

const TrackDetailsContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

export const SpotifyTrack = ({ track, previewUrl }: SpotifyTrackProps) => (
  <TrackContainer>
    <img
      src={track.images.sort((a, b) => b.width - a.width)[0].url}
      alt={`Track album art for the track "${track.name}" from the album "${track.album.name}"`}
    />
    <TrackDetailsContainer>
      <h6>{track.name}</h6>
      <p>{track.artists.map((artist) => artist.name).join(', ')}</p>
      <AudioPlayer src={previewUrl} />
    </TrackDetailsContainer>
  </TrackContainer>
);
