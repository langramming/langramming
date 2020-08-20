import * as React from 'react';
import ReactAudioPlayer from 'react-audio-player';

interface AudioPlayerProps {
  src: string;
}

export const AudioPlayer = ({ src }: AudioPlayerProps) => {
  const [audioRef, setAudioRef] = React.useState<HTMLAudioElement | null>(null);

  const togglePlayPause = React.useCallback(() => {
    if (audioRef == null) return;
    if (audioRef.paused) {
      audioRef.play();
    } else {
      audioRef.pause();
    }
  }, []);

  return (
    <>
      <ReactAudioPlayer ref={setAudioRef} src={src} />
      {audioRef && <button onClick={togglePlayPause}>{audioRef.paused ? 'Play' : 'Pause'}</button>}
    </>
  );
};
