import { IYear } from 'app/entities/year/year.model';
import { ICountry } from 'app/entities/country/country.model';
import { IFuel } from 'app/entities/fuel/fuel.model';
import { IMetadata } from 'app/entities/metadata/metadata.model';

export interface IFactTradeCapacity {
  id: string;
  value?: number | null;
  year?: IYear | null;
  country1?: ICountry | null;
  country2?: ICountry | null;
  fuel?: IFuel | null;
  metadata?: IMetadata | null;
}

export type NewFactTradeCapacity = Omit<IFactTradeCapacity, 'id'> & { id: null };
