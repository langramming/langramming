import * as React from "react";
import { useEffect } from "react";

type ActionType<T> =
  | { type: "LOADING" }
  | { type: "DATA"; data: T }
  | { type: "ERROR"; error: Error };

interface State<T> {
  loading: boolean;
  data: T | undefined;
  error: Error | undefined;
}

const reducer = <T>(prevState: State<T>, action: ActionType<T>): State<T> => {
  if (action.type === "LOADING") {
    return { loading: true, data: prevState.data, error: prevState.error };
  }
  if (action.type === "DATA") {
    return { loading: false, data: action.data, error: undefined };
  }
  if (action.type === "ERROR") {
    return { loading: false, data: undefined, error: action.error };
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
    data: undefined,
    error: undefined,
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
