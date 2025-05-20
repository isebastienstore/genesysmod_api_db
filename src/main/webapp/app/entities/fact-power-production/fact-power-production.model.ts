import { IYear } from 'app/entities/year/year.model';
import { ICountry } from 'app/entities/country/country.model';
import { ITechnology } from 'app/entities/technology/technology.model';
import { IFuel } from 'app/entities/fuel/fuel.model';
import { IMetadata } from 'app/entities/metadata/metadata.model';

export interface IFactPowerProduction {
  id: string;
  value?: number | null;
  year?: IYear | null;
  country?: ICountry | null;
  technology?: ITechnology | null;
  fuel?: IFuel | null;
  metadata?: IMetadata | null;
}

export type NewFactPowerProduction = Omit<IFactPowerProduction, 'id'> & { id: null };
