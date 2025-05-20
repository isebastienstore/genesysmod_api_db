import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMetadata, NewMetadata } from '../metadata.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMetadata for edit and NewMetadataFormGroupInput for create.
 */
type MetadataFormGroupInput = IMetadata | PartialWithRequiredKeyOf<NewMetadata>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMetadata | NewMetadata> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type MetadataFormRawValue = FormValueOf<IMetadata>;

type NewMetadataFormRawValue = FormValueOf<NewMetadata>;

type MetadataFormDefaults = Pick<NewMetadata, 'id' | 'createdAt' | 'updatedAt'>;

type MetadataFormGroupContent = {
  id: FormControl<MetadataFormRawValue['id'] | NewMetadata['id']>;
  createdBy: FormControl<MetadataFormRawValue['createdBy']>;
  updatedBy: FormControl<MetadataFormRawValue['updatedBy']>;
  action: FormControl<MetadataFormRawValue['action']>;
  createdAt: FormControl<MetadataFormRawValue['createdAt']>;
  updatedAt: FormControl<MetadataFormRawValue['updatedAt']>;
  source: FormControl<MetadataFormRawValue['source']>;
};

export type MetadataFormGroup = FormGroup<MetadataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MetadataFormService {
  createMetadataFormGroup(metadata: MetadataFormGroupInput = { id: null }): MetadataFormGroup {
    const metadataRawValue = this.convertMetadataToMetadataRawValue({
      ...this.getFormDefaults(),
      ...metadata,
    });
    return new FormGroup<MetadataFormGroupContent>({
      id: new FormControl(
        { value: metadataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createdBy: new FormControl(metadataRawValue.createdBy),
      updatedBy: new FormControl(metadataRawValue.updatedBy),
      action: new FormControl(metadataRawValue.action),
      createdAt: new FormControl(metadataRawValue.createdAt),
      updatedAt: new FormControl(metadataRawValue.updatedAt),
      source: new FormControl(metadataRawValue.source),
    });
  }

  getMetadata(form: MetadataFormGroup): IMetadata | NewMetadata {
    return this.convertMetadataRawValueToMetadata(form.getRawValue() as MetadataFormRawValue | NewMetadataFormRawValue);
  }

  resetForm(form: MetadataFormGroup, metadata: MetadataFormGroupInput): void {
    const metadataRawValue = this.convertMetadataToMetadataRawValue({ ...this.getFormDefaults(), ...metadata });
    form.reset(
      {
        ...metadataRawValue,
        id: { value: metadataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MetadataFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertMetadataRawValueToMetadata(rawMetadata: MetadataFormRawValue | NewMetadataFormRawValue): IMetadata | NewMetadata {
    return {
      ...rawMetadata,
      createdAt: dayjs(rawMetadata.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawMetadata.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertMetadataToMetadataRawValue(
    metadata: IMetadata | (Partial<NewMetadata> & MetadataFormDefaults),
  ): MetadataFormRawValue | PartialWithRequiredKeyOf<NewMetadataFormRawValue> {
    return {
      ...metadata,
      createdAt: metadata.createdAt ? metadata.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: metadata.updatedAt ? metadata.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
