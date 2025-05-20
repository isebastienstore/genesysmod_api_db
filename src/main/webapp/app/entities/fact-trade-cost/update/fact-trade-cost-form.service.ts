import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFactTradeCost, NewFactTradeCost } from '../fact-trade-cost.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactTradeCost for edit and NewFactTradeCostFormGroupInput for create.
 */
type FactTradeCostFormGroupInput = IFactTradeCost | PartialWithRequiredKeyOf<NewFactTradeCost>;

type FactTradeCostFormDefaults = Pick<NewFactTradeCost, 'id'>;

type FactTradeCostFormGroupContent = {
  id: FormControl<IFactTradeCost['id'] | NewFactTradeCost['id']>;
  fixedCost: FormControl<IFactTradeCost['fixedCost']>;
  variableCost: FormControl<IFactTradeCost['variableCost']>;
  fuel: FormControl<IFactTradeCost['fuel']>;
  metadata: FormControl<IFactTradeCost['metadata']>;
};

export type FactTradeCostFormGroup = FormGroup<FactTradeCostFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactTradeCostFormService {
  createFactTradeCostFormGroup(factTradeCost: FactTradeCostFormGroupInput = { id: null }): FactTradeCostFormGroup {
    const factTradeCostRawValue = {
      ...this.getFormDefaults(),
      ...factTradeCost,
    };
    return new FormGroup<FactTradeCostFormGroupContent>({
      id: new FormControl(
        { value: factTradeCostRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fixedCost: new FormControl(factTradeCostRawValue.fixedCost, {
        validators: [Validators.required],
      }),
      variableCost: new FormControl(factTradeCostRawValue.variableCost, {
        validators: [Validators.required],
      }),
      fuel: new FormControl(factTradeCostRawValue.fuel),
      metadata: new FormControl(factTradeCostRawValue.metadata),
    });
  }

  getFactTradeCost(form: FactTradeCostFormGroup): IFactTradeCost | NewFactTradeCost {
    return form.getRawValue() as IFactTradeCost | NewFactTradeCost;
  }

  resetForm(form: FactTradeCostFormGroup, factTradeCost: FactTradeCostFormGroupInput): void {
    const factTradeCostRawValue = { ...this.getFormDefaults(), ...factTradeCost };
    form.reset(
      {
        ...factTradeCostRawValue,
        id: { value: factTradeCostRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactTradeCostFormDefaults {
    return {
      id: null,
    };
  }
}
