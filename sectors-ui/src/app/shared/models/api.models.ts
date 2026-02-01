export interface UserAuthDTO {
  username: string;
  password: string;
}

export interface AuthResponseDTO {
  jwt: string;
}

export interface CreateApplicationDTO {
  applicantName: string;
  sectorIds: number[];
  agreedToTerms: boolean;
}

export interface UpdateApplicationDTO {
  applicantName: string;
  sectorIds: number[];
}

export interface ApplicationResponseDTO {
  applicationId: string;
  applicantName: string;
  sectorIds: number[];
  agreedToTerms: boolean;
}

export interface ApplicationSummaryResponseDTO {
  id: string;
  applicantName: string;
  createdAt: string;
  updatedAt: string | null;
}

export interface SectorResponseDTO {
  id: number;
  name: string;
  parentId: number | null;
  sectorLevel: number;
  children: SectorResponseDTO[];
}

export interface ErrorResponseDTO {
  code: string;
  description: string;
}

export interface Notification {
  message: string;
  type: 'success' | 'error';
}
