import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITurma, NewTurma } from '../turma.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITurma for edit and NewTurmaFormGroupInput for create.
 */
type TurmaFormGroupInput = ITurma | PartialWithRequiredKeyOf<NewTurma>;

type TurmaFormDefaults = Pick<NewTurma, 'id' | 'professors'>;

type TurmaFormGroupContent = {
  id: FormControl<ITurma['id'] | NewTurma['id']>;
  nomeTurma: FormControl<ITurma['nomeTurma']>;
  descricao: FormControl<ITurma['descricao']>;
  professors: FormControl<ITurma['professors']>;
};

export type TurmaFormGroup = FormGroup<TurmaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TurmaFormService {
  createTurmaFormGroup(turma: TurmaFormGroupInput = { id: null }): TurmaFormGroup {
    const turmaRawValue = {
      ...this.getFormDefaults(),
      ...turma,
    };
    return new FormGroup<TurmaFormGroupContent>({
      id: new FormControl(
        { value: turmaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomeTurma: new FormControl(turmaRawValue.nomeTurma, {
        validators: [Validators.required],
      }),
      descricao: new FormControl(turmaRawValue.descricao),
      professors: new FormControl(turmaRawValue.professors ?? []),
    });
  }

  getTurma(form: TurmaFormGroup): ITurma | NewTurma {
    return form.getRawValue() as ITurma | NewTurma;
  }

  resetForm(form: TurmaFormGroup, turma: TurmaFormGroupInput): void {
    const turmaRawValue = { ...this.getFormDefaults(), ...turma };
    form.reset(
      {
        ...turmaRawValue,
        id: { value: turmaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TurmaFormDefaults {
    return {
      id: null,
      professors: [],
    };
  }
}
