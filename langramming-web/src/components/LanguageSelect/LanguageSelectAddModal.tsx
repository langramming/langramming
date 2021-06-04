import * as React from 'react';
import { Button, Dialog, Pane, TextInputField } from 'evergreen-ui';
import { Language } from 'types/Language';
import { useForm } from 'react-hook-form';

interface LanguageSelectCreateModalProps {
  isSaving: boolean;
  initialName: string;
  onAdd: (value: Language) => void;
  onClose: () => void;
}

export const LanguageSelectAddModal = ({
  isSaving,
  initialName,
  onAdd,
  onClose,
}: LanguageSelectCreateModalProps): JSX.Element => {
  const { register, handleSubmit, formState } = useForm<{ code: string; name: string }>({
    mode: 'onBlur',
    reValidateMode: 'onBlur',
    defaultValues: {
      code: '',
      name: initialName,
    },
  });

  const onSubmit = React.useCallback(
    (values: { code: string; name: string }) => {
      onAdd(values);
      onClose();
    },
    [onAdd, onClose],
  );

  // TODO: figure out what errors look like and add validation errors
  console.log(formState.errors);

  return (
    <Dialog
      isShown
      preventBodyScrolling
      shouldCloseOnEscapePress
      shouldCloseOnOverlayClick
      title="Add new language"
      hasFooter={false}
    >
      <form onSubmit={handleSubmit(onSubmit)}>
        <main>
          <TextInputField
            label="Name"
            autoFocus
            hint="The name of the language, e.g. English or French"
            {...register('name', {
              required: true,
              validate: (name) => name.trim() !== '',
            })}
          />
          <TextInputField
            label="Code"
            hint="The ISO code for the language, e.g. en or fr"
            {...register('code', {
              required: true,
              validate: (code) => code.trim() !== '',
            })}
          />
        </main>
        <footer>
          <Pane display="flex" flexDirection="row" justifyContent="flex-end">
            <Button onClick={onClose}>Cancel</Button>
            <Button appearance="primary" isLoading={isSaving} type="submit">
              Add
            </Button>
          </Pane>
        </footer>
      </form>
    </Dialog>
  );
};
