import * as React from "react";

import Button, { ButtonGroup } from "@atlaskit/button";
import Form, { Field, ErrorMessage, HelperMessage } from "@atlaskit/form";
import ModalDialog, {
  ModalFooter,
  ModalTransition,
} from "@atlaskit/modal-dialog";
import { ContainerProps } from "@atlaskit/modal-dialog/components/Container";
import Textfield from "@atlaskit/textfield";

interface LanguageSelectCreateModalProps {
  newOption: string | null;
  onRegister: (value: { name: string; code: string }) => void;
  onClose: () => void;
}

export const LanguageSelectRegisterModal = ({
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
          Container({ children }: ContainerProps) {
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
                    <Button appearance="primary" type="submit">
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
              <Textfield {...fieldProps} placeholder="Language name" />
              {!error && (
                <HelperMessage>{"Enter the language name here"}</HelperMessage>
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
                <HelperMessage>{"Enter the language code here"}</HelperMessage>
              )}
              {error && <ErrorMessage>{error}</ErrorMessage>}
            </>
          )}
        </Field>
      </ModalDialog>
    )}
  </ModalTransition>
);
