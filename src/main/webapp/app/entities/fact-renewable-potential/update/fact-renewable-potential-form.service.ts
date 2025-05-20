import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFactRenewablePotential, NewFactRenewablePotential } from '../fact-renewable-potential.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactRenewablePotential for edit and NewFactRenewablePotentialFormGroupInput for create.
 */
type FactRenewablePotentialFormGroupInput = IFactRenewablePotential | PartialWithRequiredKeyOf<NewFactRenewablePotential>;

type FactRenewablePotentialFormDefaults = Pick<NewFactRenewablePotential, 'id'>;

type FactRenewablePotentialFormGroupContent = {
  id: FormControl<IFactRenewablePotential['id'] | NewFactRenewablePotential['id']>;
  maxCapacity: FormControl<IFactRenewablePotential['maxCapacity']>;
  availableCapacity: FormControl<IFactRenewablePotential['availableCapacity']>;
  minCapacity: FormControl<IFactRenewablePotential['minCapacity']>;
  country: FormControl<IFactRenewablePotential['country']>;
  year: FormControl<IFactRenewablePotential['year']>;
  technology: FormControl<IFactRenewablePotential['technology']>;
  metadata: FormControl<IFactRenewablePotential['metadata']>;
};

export type FactRenewablePotentialFormGroup = FormGroup<FactRenewablePotentialFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactRenewablePotentialFormService {
  createFactRenewablePotentialFormGroup(
    factRenewablePotential: FactRenewablePotentialFormGroupInput = { id: null },
  ): FactRenewablePotentialFormGroup {
    const factRenewablePotentialRawValue = {
      ...this.getFormDefaults(),
      ...factRenewablePotential,
    };
    return new FormGroup<FactRenewablePotentialFormGroupContent>({
      id: new FormControl(
        { value: factRenewablePotentialRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      maxCapacity: new FormControl(factRenewablePotentialRawValue.maxCapacity, {
        validators: [Validators.required],
      }),
      availableCapacity: new FormControl(factRenewablePotentialRawValue.availableCapacity, {
        validators: [Validators.required],
      }),
      minCapacity: new FormControl(factRenewablePotentialRawValue.minCapacity, {
        validators: [Validators.required],
      }),
      country: new FormControl(factRenewablePotentialRawValue.country),
      year: new FormControl(factRenewablePotentialRawValue.year),
      technology: new FormControl(factRenewablePotentialRawValue.technology),
      metadata: new FormControl(factRenewablePotentialRawValue.metadata),
    });
  }

  getFactRenewablePotential(form: FactRenewablePotentialFormGroup): IFactRenewablePotential | NewFactRenewablePotential {
    return form.getRawValue() as IFactRenewablePotential | NewFactRenewablePotential;
  }

  resetForm(form: FactRenewablePotentialFormGroup, factRenewablePotential: FactRenewablePotentialFormGroupInput): void {
    const factRenewablePotentialRawValue = { ...this.getFormDefaults(), ...factRenewablePotential };
    form.reset(
      {
        ...factRenewablePotentialRawValue,
        id: { value: factRenewablePotentialRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactRenewablePotentialFormDefaults {
    return {
      id: null,
    };
  }
}
