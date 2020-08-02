import * as React from "react";
import { ValueType } from "react-select";
import { CreatableSelect } from "@atlaskit/select";

import { Language, LanguageResponse } from "../../types/Language";
import { useFetch } from "../../common/fetch/useFetch";

import { LanguageSelectRegisterModal } from "./LanguageSelectRegisterModal";
import { isValid, validate } from "./utils";

export interface LanguageSelectProps {
  autoFocus?: boolean;
  defaultValue?: Language;
  onChange: (language: Language | null | undefined) => void;
}

interface LanguageOption {
  label: string;
  value: Language;
}

interface LanguageSelectState {
  filterString: string;

  newOptions: Language[];
  newOption: string | null;
}

type LanguageSelectStateAction =
  | { type: "CREATE_START"; name: string }
  | { type: "CREATE_SAVE"; language: Language }
  | { type: "CREATE_CANCEL" }
  | { type: "FILTER"; filterString: string };

const languageSelectReducer: React.Reducer<
  LanguageSelectState,
  LanguageSelectStateAction
> = (prevState, action) => {
  switch (action.type) {
    case "CREATE_START": {
      return {
        ...prevState,
        newOption: action.name,
      };
    }
    case "CREATE_SAVE": {
      return {
        ...prevState,
        newOption: null,
        newOptions: [...prevState.newOptions, action.language],
      };
    }
    case "CREATE_CANCEL": {
      return {
        ...prevState,
        newOption: null,
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
  filterString: "",
};

export const LanguageSelect = ({
  autoFocus,
  defaultValue,
  onChange,
}: LanguageSelectProps): JSX.Element => {
  const languageApiState = useFetch<LanguageResponse>("/api/language");

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

  const handleOnCreate = React.useCallback((inputValue: string) => {
    dispatch({ type: "CREATE_START", name: inputValue });
  }, []);

  const handleOnCreateSave = React.useCallback((language: Language): {
    language?: string;
    name?: string;
  } | void => {
    const validationResult = validate(language);
    if (!isValid(validationResult)) {
      return validationResult;
    }

    dispatch({ type: "CREATE_SAVE", language });
    fetch("/api/language", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(language),
    });
  }, []);

  const handleOnCreateCancel = React.useCallback(
    () => dispatch({ type: "CREATE_CANCEL" }),
    []
  );

  const handleOnInputChange = React.useCallback((inputValue: string) => {
    dispatch({ type: "FILTER", filterString: inputValue });
  }, []);

  const languageOptions = React.useMemo(
    () => [
      ...(languageApiState.data?.languages ?? []).map((language) => ({
        label: language.name,
        value: language,
      })),
      ...state.newOptions.map((language) => ({
        label: language.name,
        value: language,
      })),
    ],
    [languageApiState.data, state.newOptions]
  );

  return (
    <>
      <CreatableSelect<LanguageOption>
        allowCreateWhileLoading={false}
        autoFocus={autoFocus}
        closeMenuOnSelect
        defaultValue={defaultOption}
        formatOptionLabel={(option) =>
          option.value.code != null
            ? `${option.label} (${option.value.code})`
            : option.label
        }
        inputValue={state.filterString}
        onChange={handleOnChange}
        onCreateOption={handleOnCreate}
        onInputChange={handleOnInputChange}
        options={languageOptions}
        placeholder="Select a language..."
      />
      <LanguageSelectRegisterModal
        newOption={state.newOption}
        onRegister={handleOnCreateSave}
        onClose={handleOnCreateCancel}
      />
    </>
  );
};
