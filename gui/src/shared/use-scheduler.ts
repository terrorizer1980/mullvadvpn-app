import { useEffect, useMemo } from 'react';
import { Scheduler } from './scheduler';

export function useScheduler() {
  const closeScheduler = useMemo(() => new Scheduler(), []);

  useEffect(() => {
    return () => closeScheduler.cancel();
  }, []);

  return closeScheduler;
}
