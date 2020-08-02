import { Language } from "../../types/Language";

type LanguageValidation = { [K in keyof Language]: string | undefined };

const validateCode = (code: string): string | undefined => {
  if (code.trim() === "") {
    return "Language code cannot be empty";
  }
  if (!/[a-z]{2,3}(_[A-Z]{2,3})?/.test(code)) {
    return "Language code must be like en or en_US";
  }
  return undefined;
};

export const validate = (language: Language): LanguageValidation => {
  return {
    name:
      language.name.trim() === "" ? "Language name cannot be empty" : undefined,
    code: validateCode(language.code),
  };
};

export const isValid = (validationResult: LanguageValidation): boolean => {
  return [validationResult.name, validationResult.code].every(
    (message) => message === undefined
  );
};