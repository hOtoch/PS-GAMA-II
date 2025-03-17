import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILocalizacao, NewLocalizacao } from '../localizacao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocalizacao for edit and NewLocalizacaoFormGroupInput for create.
 */
type LocalizacaoFormGroupInput = ILocalizacao | PartialWithRequiredKeyOf<NewLocalizacao>;

type LocalizacaoFormDefaults = Pick<NewLocalizacao, 'id'>;

type LocalizacaoFormGroupContent = {
  id: FormControl<ILocalizacao['id'] | NewLocalizacao['id']>;
  endereco: FormControl<ILocalizacao['endereco']>;
  cidade: FormControl<ILocalizacao['cidade']>;
  estado: FormControl<ILocalizacao['estado']>;
  cep: FormControl<ILocalizacao['cep']>;
};

export type LocalizacaoFormGroup = FormGroup<LocalizacaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocalizacaoFormService {
  createLocalizacaoFormGroup(localizacao: LocalizacaoFormGroupInput = { id: null }): LocalizacaoFormGroup {
    const localizacaoRawValue = {
      ...this.getFormDefaults(),
      ...localizacao,
    };
    return new FormGroup<LocalizacaoFormGroupContent>({
      id: new FormControl(
        { value: localizacaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      endereco: new FormControl(localizacaoRawValue.endereco, {
        validators: [Validators.required],
      }),
      cidade: new FormControl(localizacaoRawValue.cidade, {
        validators: [Validators.required],
      }),
      estado: new FormControl(localizacaoRawValue.estado, {
        validators: [Validators.required],
      }),
      cep: new FormControl(localizacaoRawValue.cep, {
        validators: [Validators.required],
      }),
    });
  }

  getLocalizacao(form: LocalizacaoFormGroup): ILocalizacao | NewLocalizacao {
    return form.getRawValue() as ILocalizacao | NewLocalizacao;
  }

  resetForm(form: LocalizacaoFormGroup, localizacao: LocalizacaoFormGroupInput): void {
    const localizacaoRawValue = { ...this.getFormDefaults(), ...localizacao };
    form.reset(
      {
        ...localizacaoRawValue,
        id: { value: localizacaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LocalizacaoFormDefaults {
    return {
      id: null,
    };
  }
}
