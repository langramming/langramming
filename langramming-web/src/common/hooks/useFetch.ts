import * as React from 'react';
import { useEffect } from 'react';
import { useAppContext } from './useAppContext';

type ActionType<T> =
  | { type: 'LOADING' }
  | { type: 'DATA'; data: T }
  | { type: 'ERROR'; error: Error };

export interface FetchState<T> {
  loading: boolean;
  data: T | null;
  error: Error | null;
}

const reducer = <T>(prevState: FetchState<T>, action: ActionType<T>): FetchState<T> => {
  if (action.type === 'LOADING') {
    return { ...prevState, loading: true };
  }
  if (action.type === 'DATA') {
    return { ...prevState, loading: false, data: action.data, error: null };
  }
  if (action.type === 'ERROR') {
    return { ...prevState, loading: false, error: action.error };
  }
  return prevState;
};

export const useFetch = <T>(uri: string): FetchState<T> => {
  const [{ loading, data, error }, dispatch] = React.useReducer<
    (prevState: FetchState<T>, action: ActionType<T>) => FetchState<T>
  >(reducer, {
    loading: false,
    data: null,
    error: null,
  });
  const { baseUrl } = useAppContext();

  useEffect(() => {
    dispatch({ type: 'LOADING' });
    fetch(`${baseUrl}${uri}`)
      .then((response) => response.json())
      .then((data) => dispatch({ type: 'DATA', data }))
      .catch((error) => dispatch({ type: 'ERROR', error }));
  }, [baseUrl, uri]);

  return { loading, data, error };
};
