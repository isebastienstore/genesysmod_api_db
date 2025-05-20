import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFactPowerProduction, NewFactPowerProduction } from '../fact-power-production.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactPowerProduction for edit and NewFactPowerProductionFormGroupInput for create.
 */
type FactPowerProductionFormGroupInput = IFactPowerProduction | PartialWithRequiredKeyOf<NewFactPowerProduction>;

type FactPowerProductionFormDefaults = Pick<NewFactPowerProduction, 'id'>;

type FactPowerProductionFormGroupContent = {
  id: FormControl<IFactPowerProduction['id'] | NewFactPowerProduction['id']>;
  value: FormControl<IFactPowerProduction['value']>;
  year: FormControl<IFactPowerProduction['year']>;
  country: FormControl<IFactPowerProduction['country']>;
  technology: FormControl<IFactPowerProduction['technology']>;
  fuel: FormControl<IFactPowerProduction['fuel']>;
  metadata: FormControl<IFactPowerProduction['metadata']>;
};

export type FactPowerProductionFormGroup = FormGroup<FactPowerProductionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactPowerProductionFormService {
  createFactPowerProductionFormGroup(factPowerProduction: FactPowerProductionFormGroupInput = { id: null }): FactPowerProductionFormGroup {
    const factPowerProductionRawValue = {
      ...this.getFormDefaults(),
      ...factPowerProduction,
    };
    return new FormGroup<FactPowerProductionFormGroupContent>({
      id: new FormControl(
        { value: factPowerProductionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      value: new FormControl(factPowerProductionRawValue.value, {
        validators: [Validators.required],
      }),
      year: new FormControl(factPowerProductionRawValue.year),
      country: new FormControl(factPowerProductionRawValue.country),
      technology: new FormControl(factPowerProductionRawValue.technology),
      fuel: new FormControl(factPowerProductionRawValue.fuel),
      metadata: new FormControl(factPowerProductionRawValue.metadata),
    });
  }

  getFactPowerProduction(form: FactPowerProductionFormGroup): IFactPowerProduction | NewFactPowerProduction {
    return form.getRawValue() as IFactPowerProduction | NewFactPowerProduction;
  }

  resetForm(form: FactPowerProductionFormGroup, factPowerProduction: FactPowerProductionFormGroupInput): void {
    const factPowerProductionRawValue = { ...this.getFormDefaults(), ...factPowerProduction };
    form.reset(
      {
        ...factPowerProductionRawValue,
        id: { value: factPowerProductionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactPowerProductionFormDefaults {
    return {
      id: null,
    };
  }
}
