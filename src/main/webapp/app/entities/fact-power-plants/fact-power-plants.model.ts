import { IYear } from 'app/entities/year/year.model';
import { ICountry } from 'app/entities/country/country.model';
import { ITechnology } from 'app/entities/technology/technology.model';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { StatusType } from 'app/entities/enumerations/status-type.model';

export interface IFactPowerPlants {
  id: string;
  name?: string | null;
  intalledCapacity?: number | null;
  availabilityCapacity?: number | null;
  status?: keyof typeof StatusType | null;
  commissioningDate?: IYear | null;
  decommissioningDate?: IYear | null;
  country?: ICountry | null;
  technology?: ITechnology | null;
  metadata?: IMetadata | null;
}

export type NewFactPowerPlants = Omit<IFactPowerPlants, 'id'> & { id: null };
