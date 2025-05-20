import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFactPowerPlants, NewFactPowerPlants } from '../fact-power-plants.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactPowerPlants for edit and NewFactPowerPlantsFormGroupInput for create.
 */
type FactPowerPlantsFormGroupInput = IFactPowerPlants | PartialWithRequiredKeyOf<NewFactPowerPlants>;

type FactPowerPlantsFormDefaults = Pick<NewFactPowerPlants, 'id'>;

type FactPowerPlantsFormGroupContent = {
  id: FormControl<IFactPowerPlants['id'] | NewFactPowerPlants['id']>;
  name: FormControl<IFactPowerPlants['name']>;
  intalledCapacity: FormControl<IFactPowerPlants['intalledCapacity']>;
  availabilityCapacity: FormControl<IFactPowerPlants['availabilityCapacity']>;
  status: FormControl<IFactPowerPlants['status']>;
  commissioningDate: FormControl<IFactPowerPlants['commissioningDate']>;
  decommissioningDate: FormControl<IFactPowerPlants['decommissioningDate']>;
  country: FormControl<IFactPowerPlants['country']>;
  technology: FormControl<IFactPowerPlants['technology']>;
  metadata: FormControl<IFactPowerPlants['metadata']>;
};

export type FactPowerPlantsFormGroup = FormGroup<FactPowerPlantsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactPowerPlantsFormService {
  createFactPowerPlantsFormGroup(factPowerPlants: FactPowerPlantsFormGroupInput = { id: null }): FactPowerPlantsFormGroup {
    const factPowerPlantsRawValue = {
      ...this.getFormDefaults(),
      ...factPowerPlants,
    };
    return new FormGroup<FactPowerPlantsFormGroupContent>({
      id: new FormControl(
        { value: factPowerPlantsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(factPowerPlantsRawValue.name, {
        validators: [Validators.required],
      }),
      intalledCapacity: new FormControl(factPowerPlantsRawValue.intalledCapacity, {
        validators: [Validators.required],
      }),
      availabilityCapacity: new FormControl(factPowerPlantsRawValue.availabilityCapacity, {
        validators: [Validators.required],
      }),
      status: new FormControl(factPowerPlantsRawValue.status),
      commissioningDate: new FormControl(factPowerPlantsRawValue.commissioningDate),
      decommissioningDate: new FormControl(factPowerPlantsRawValue.decommissioningDate),
      country: new FormControl(factPowerPlantsRawValue.country),
      technology: new FormControl(factPowerPlantsRawValue.technology),
      metadata: new FormControl(factPowerPlantsRawValue.metadata),
    });
  }

  getFactPowerPlants(form: FactPowerPlantsFormGroup): IFactPowerPlants | NewFactPowerPlants {
    return form.getRawValue() as IFactPowerPlants | NewFactPowerPlants;
  }

  resetForm(form: FactPowerPlantsFormGroup, factPowerPlants: FactPowerPlantsFormGroupInput): void {
    const factPowerPlantsRawValue = { ...this.getFormDefaults(), ...factPowerPlants };
    form.reset(
      {
        ...factPowerPlantsRawValue,
        id: { value: factPowerPlantsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactPowerPlantsFormDefaults {
    return {
      id: null,
    };
  }
}
