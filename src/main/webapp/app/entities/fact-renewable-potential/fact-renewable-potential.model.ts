import { ICountry } from 'app/entities/country/country.model';
import { IYear } from 'app/entities/year/year.model';
import { ITechnology } from 'app/entities/technology/technology.model';
import { IMetadata } from 'app/entities/metadata/metadata.model';

export interface IFactRenewablePotential {
  id: string;
  maxCapacity?: number | null;
  availableCapacity?: number | null;
  minCapacity?: number | null;
  country?: ICountry | null;
  year?: IYear | null;
  technology?: ITechnology | null;
  metadata?: IMetadata | null;
}

export type NewFactRenewablePotential = Omit<IFactRenewablePotential, 'id'> & { id: null };
