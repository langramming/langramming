import * as React from "react";

import Button, { ButtonGroup } from "@atlaskit/button";
import Form, { Field, ErrorMessage, HelperMessage } from "@atlaskit/form";
import ModalDialog, {
  ContainerComponentProps,
  ModalFooter,
  ModalTransition,
} from "@atlaskit/modal-dialog";
import Textfield from "@atlaskit/textfield";

interface LanguageSelectCreateModalProps {
  isSaving: boolean;
  newOption: string | null;
  onRegister: (value: { name: string; code: string }) => void;
  onClose: () => void;
}

export const LanguageSelectRegisterModal = ({
  isSaving,
  newOption,
  onRegister,
  onClose,
}: LanguageSelectCreateModalProps): JSX.Element => (
  <ModalTransition>
    {newOption != null && (
      <ModalDialog
        shouldCloseOnEscapePress
        shouldCloseOnOverlayClick
        heading="Register new language"
        onClose={onClose}
        components={{
          Container({ children }: ContainerComponentProps) {
            return (
              <Form onSubmit={onRegister}>
                {({ formProps }) => <form {...formProps}>{children}</form>}
              </Form>
            );
          },
          Footer() {
            return (
              <ModalFooter>
                <div
                  style={{
                    display: "flex",
                    flex: "0 0 auto",
                    justifyContent: "flex-end",
                    width: "100%",
                  }}
                >
                  <ButtonGroup>
                    <Button
                      appearance="primary"
                      type="submit"
                      isLoading={isSaving}
                    >
                      Register
                    </Button>
                    <Button appearance="default" onClick={onClose}>
                      Cancel
                    </Button>
                  </ButtonGroup>
                </div>
              </ModalFooter>
            );
          },
        }}
      >
        <Field name="name" isRequired defaultValue={newOption ?? ""}>
          {({ fieldProps, error }) => (
            <>
              <Textfield
                {...fieldProps}
                placeholder="Language name"
                autoFocus
              />
              {!error && (
                <HelperMessage>
                  {"The language name, e.g. English"}
                </HelperMessage>
              )}
              {error && <ErrorMessage>{error}</ErrorMessage>}
            </>
          )}
        </Field>
        <Field name="code" isRequired defaultValue="">
          {({ fieldProps, error }) => (
            <>
              <Textfield {...fieldProps} placeholder="Language code" />
              {!error && (
                <HelperMessage>
                  {"The language code, e.g. en or en_US"}
                </HelperMessage>
              )}
              {error && <ErrorMessage>{error}</ErrorMessage>}
            </>
          )}
        </Field>
      </ModalDialog>
    )}
  </ModalTransition>
);
