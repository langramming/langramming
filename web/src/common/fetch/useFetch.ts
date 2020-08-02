import * as React from "react";
import { useEffect } from "react";

type ActionType<T> =
  | { type: "LOADING" }
  | { type: "DATA"; data: T }
  | { type: "ERROR"; error: Error };

interface State<T> {
  loading: boolean;
  data: T | null;
  error: Error | null;
}

const reducer = <T>(prevState: State<T>, action: ActionType<T>): State<T> => {
  if (action.type === "LOADING") {
    return { ...prevState, loading: true };
  }
  if (action.type === "DATA") {
    return { ...prevState, loading: false, data: action.data, error: null };
  }
  if (action.type === "ERROR") {
    return { ...prevState, loading: false, error: action.error };
  }
  return prevState;
};

export const useFetch = <T>(uri: string): State<T> => {
  const castReducer = reducer as (
    prevState: State<T>,
    action: ActionType<T>
  ) => State<T>;
  const [{ loading, data, error }, dispatch] = React.useReducer(castReducer, {
    loading: false,
    data: null,
    error: null,
  });

  useEffect(() => {
    if (!loading && !data && !error) {
      dispatch({ type: "LOADING" });
      fetch(uri)
        .then((response) => response.json())
        .then((data) => dispatch({ type: "DATA", data }))
        .catch((error) => dispatch({ type: "ERROR", error }));
    }
  }, [loading, data, error, dispatch]);

  return { loading, data, error };
};
