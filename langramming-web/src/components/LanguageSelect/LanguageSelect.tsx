import * as React from 'react';
import { ValueType } from 'react-select';
import { CreatableSelect } from '@atlaskit/select';

import { Language, LanguageResponse } from '../../types/Language';
import { useFetch } from '../../common/hooks/useFetch';

import { LanguageSelectAddModal } from './LanguageSelectAddModal';
import { createLanguage, isValid, validate } from './utils';
import { useAppContext } from '../../common/hooks/useAppContext';

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
  isSaving: boolean;
  newOptions: Language[];
  newOption: string | null;
  selectedOption: LanguageOption | null | undefined;
}

type LanguageSelectStateAction =
  | { type: 'CREATE_START'; name: string }
  | { type: 'CREATE_SAVE_BEGIN' }
  | { type: 'CREATE_SAVE_SUCCESS'; language: Language }
  | { type: 'CREATE_SAVE_FAIL' }
  | { type: 'CREATE_CANCEL' }
  | { type: 'SELECT'; selectedOption: LanguageOption | null | undefined };

const languageSelectReducer: React.Reducer<LanguageSelectState, LanguageSelectStateAction> = (
  prevState,
  action,
) => {
  switch (action.type) {
    case 'CREATE_START': {
      return {
        ...prevState,
        newOption: action.name,
      };
    }
    case 'CREATE_SAVE_BEGIN': {
      return {
        ...prevState,
        isSaving: true,
      };
    }
    case 'CREATE_SAVE_FAIL': {
      return {
        ...prevState,
        isSaving: false,
      };
    }
    case 'CREATE_SAVE_SUCCESS': {
      return {
        ...prevState,
        isSaving: false,
        newOption: null,
        newOptions: [...prevState.newOptions, action.language],
      };
    }
    case 'CREATE_CANCEL': {
      return {
        ...prevState,
        newOption: null,
      };
    }
    case 'SELECT': {
      return {
        ...prevState,
        selectedOption: action.selectedOption,
      };
    }
  }
};

export const LanguageSelect = ({
  autoFocus,
  defaultValue,
  onChange,
}: LanguageSelectProps): JSX.Element => {
  const languageApiState = useFetch<LanguageResponse>('/api/language');
  const { baseUrl } = useAppContext();

  const [state, dispatch] = React.useReducer(languageSelectReducer, {
    isSaving: false,
    selectedOption: defaultValue ? { label: defaultValue.name, value: defaultValue } : null,
    newOption: null,
    newOptions: [],
  });

  const handleOnChange = React.useCallback(
    (value: ValueType<LanguageOption, false>) => {
      dispatch({ type: 'SELECT', selectedOption: value });
      onChange(value?.value);
    },
    [onChange],
  );

  const handleOnCreate = React.useCallback((inputValue: string) => {
    dispatch({ type: 'CREATE_START', name: inputValue });
  }, []);

  const handleOnCreateSave = React.useCallback(
    (
      language: Language,
    ): {
      language?: string;
      name?: string;
    } | void => {
      const validationResult = validate(language);
      if (!isValid(validationResult)) {
        return validationResult;
      }

      dispatch({ type: 'CREATE_SAVE_BEGIN' });
      createLanguage(language, { baseUrl })
        .then(() => {
          dispatch({ type: 'CREATE_SAVE_SUCCESS', language });
          handleOnChange({ label: language.name, value: language });
        })
        .catch(() => {
          dispatch({ type: 'CREATE_SAVE_FAIL' });
        });
    },
    [baseUrl, handleOnChange],
  );

  const handleOnCreateCancel = React.useCallback(() => dispatch({ type: 'CREATE_CANCEL' }), []);

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
    [languageApiState.data, state.newOptions],
  );

  return (
    <>
      <CreatableSelect<LanguageOption>
        allowCreateWhileLoading={false}
        autoFocus={autoFocus}
        closeMenuOnSelect
        formatCreateLabel={(inputValue: string) => `Add "${inputValue}"`}
        formatOptionLabel={(option) =>
          option.value.code != null ? `${option.label} (${option.value.code})` : option.label
        }
        onChange={handleOnChange}
        onCreateOption={handleOnCreate}
        options={languageOptions}
        placeholder="Select a language..."
        value={state.selectedOption}
      />
      <LanguageSelectAddModal
        isSaving={state.isSaving}
        newOption={state.newOption}
        onAdd={handleOnCreateSave}
        onClose={handleOnCreateCancel}
      />
    </>
  );
};
