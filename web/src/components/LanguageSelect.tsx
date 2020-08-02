import * as React from "react";
import { ValueType } from 'react-select';
import { CreatableSelect } from "@atlaskit/select";

import { Language, LanguageResponse } from "../types/Language";
import { useFetch } from "../common/fetch/useFetch";

export interface LanguageSelectProps {
  autoFocus?: boolean;
  defaultValue?: Language;
  onChange: (language: Language | null | undefined) => undefined;
}

interface LanguageOption {
  label: string;
  value: Language;
}

interface LanguageSelectState {
  filterString: string;

  newOptions: Language[];
  newOption: {
    name: string;
    code: string;
  } | null;
}

type LanguageSelectStateAction =
  | { type: "CREATE_SELECT"; name: string }
  | { type: "CREATE_SAVE"; name: string; code: string }
  | { type: "FILTER"; filterString: string };

const languageSelectReducer: React.Reducer<
  LanguageSelectState,
  LanguageSelectStateAction
> = (prevState, action) => {
  switch (action.type) {
    case "CREATE_SELECT": {
      return {
        ...prevState,
        newOption: {
          name: action.name,
          code: '',
        },
      };
    }
    case "CREATE_SAVE": {
      return {
        ...prevState,
        newOption: null,
        newOptions: [
          ...prevState.newOptions,
          prevState.newOption! // eslint-disable-line @typescript-eslint/no-non-null-assertion
        ],
      };
    }
    case "FILTER": {
      return {
        ...prevState,
        filterString: action.filterString,
      };
    }
  }
};

const initialLanguageSelectState: LanguageSelectState = {
  newOption: null,
  newOptions: [],
  filterString: '',
};

export const LanguageSelect = ({
  autoFocus,
  defaultValue,
  onChange,
}: LanguageSelectProps): JSX.Element => {
  const languageApiState = useFetch<LanguageResponse>('/api/language');

  const [state, dispatch] = React.useReducer(
    languageSelectReducer,
    initialLanguageSelectState
  );

  const defaultOption: LanguageOption | null = React.useMemo(() => {
    return defaultValue == null
      ? null
      : {
          label: `${defaultValue.name} (${defaultValue.code})`,
          value: defaultValue,
        };
  }, [defaultValue]);

  const handleOnChange = React.useCallback(
    (value: ValueType<LanguageOption>) => {
      if (value == null || "value" in value) {
        onChange(value?.value);
      } else {
        onChange(value[0].value);
      }
    },
    [onChange]
  );

  const handleOnCreate = React.useCallback(
    (inputValue: string) => {
      dispatch({ type: 'CREATE_SELECT', name: inputValue });
    },
    [],
  );

  const handleOnInputChange = React.useCallback(
    (inputValue: string) => {
      dispatch({ type: 'FILTER', filterString: inputValue });
    },
    [],
  );

  const languageOptions = React.useMemo(() => [
    ...(languageApiState.data?.languages ?? []).map(language => ({ label: language.name, value: language })),
    ...state.newOptions.map(language => ({ label: language.name, value: language })),
  ], [languageApiState.data, state.newOptions]);

  return (
    <>
      <CreatableSelect<LanguageOption>
        allowCreateWhileLoading={false}
        autoFocus={autoFocus}
        closeMenuOnSelect
        defaultValue={defaultOption}
        inputValue={state.filterString}
        onChange={handleOnChange}
        onCreateOption={handleOnCreate}
        onInputChange={handleOnInputChange}
        options={languageOptions}
      />
      {state.newOption != null && <>{JSON.stringify(state.newOption)}</>}
    </>
  );
};
