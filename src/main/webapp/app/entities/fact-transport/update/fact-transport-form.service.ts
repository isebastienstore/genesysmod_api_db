import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFactTransport, NewFactTransport } from '../fact-transport.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactTransport for edit and NewFactTransportFormGroupInput for create.
 */
type FactTransportFormGroupInput = IFactTransport | PartialWithRequiredKeyOf<NewFactTransport>;

type FactTransportFormDefaults = Pick<NewFactTransport, 'id'>;

type FactTransportFormGroupContent = {
  id: FormControl<IFactTransport['id'] | NewFactTransport['id']>;
  value: FormControl<IFactTransport['value']>;
  typeOfMobility: FormControl<IFactTransport['typeOfMobility']>;
  year: FormControl<IFactTransport['year']>;
  country: FormControl<IFactTransport['country']>;
  metadata: FormControl<IFactTransport['metadata']>;
};

export type FactTransportFormGroup = FormGroup<FactTransportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactTransportFormService {
  createFactTransportFormGroup(factTransport: FactTransportFormGroupInput = { id: null }): FactTransportFormGroup {
    const factTransportRawValue = {
      ...this.getFormDefaults(),
      ...factTransport,
    };
    return new FormGroup<FactTransportFormGroupContent>({
      id: new FormControl(
        { value: factTransportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      value: new FormControl(factTransportRawValue.value, {
        validators: [Validators.required],
      }),
      typeOfMobility: new FormControl(factTransportRawValue.typeOfMobility, {
        validators: [Validators.required],
      }),
      year: new FormControl(factTransportRawValue.year),
      country: new FormControl(factTransportRawValue.country),
      metadata: new FormControl(factTransportRawValue.metadata),
    });
  }

  getFactTransport(form: FactTransportFormGroup): IFactTransport | NewFactTransport {
    return form.getRawValue() as IFactTransport | NewFactTransport;
  }

  resetForm(form: FactTransportFormGroup, factTransport: FactTransportFormGroupInput): void {
    const factTransportRawValue = { ...this.getFormDefaults(), ...factTransport };
    form.reset(
      {
        ...factTransportRawValue,
        id: { value: factTransportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactTransportFormDefaults {
    return {
      id: null,
    };
  }
}
