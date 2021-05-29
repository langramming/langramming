import * as React from 'react';
import ReactAudioPlayer from 'react-audio-player';

interface AudioPlayerProps {
  src: string;
}

export const AudioPlayer = ({ src }: AudioPlayerProps) => {
  const [audioPlayerRef, setAudioPlayerRef] = React.useState<ReactAudioPlayer | null>(null);
  const [canPlay, setCanPlay] = React.useState<boolean>(false);
  const [isPlaying, setIsPlaying] = React.useState<boolean>(false);

  const togglePlayPause = React.useCallback(() => {
    const audioPlayerEl = audioPlayerRef?.audioEl.current;
    if (audioPlayerEl == null) {
      return;
    }

    if (audioPlayerEl.paused) {
      audioPlayerEl.play();
      setIsPlaying(true);
    } else {
      audioPlayerEl.pause();
      setIsPlaying(false);
    }
  }, [audioPlayerRef]);

  return (
    <>
      <ReactAudioPlayer
        ref={setAudioPlayerRef}
        src={src}
        autoPlay={isPlaying}
        onCanPlay={() => setCanPlay(true)}
        onPlay={() => setIsPlaying(true)}
        onPause={() => setIsPlaying(false)}
      />
      {canPlay && <button onClick={togglePlayPause}>{isPlaying ? 'Pause' : 'Play'}</button>}
    </>
  );
};
