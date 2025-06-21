export interface Policy {
  policyId: number;
  paymentId: number;
  policyNumber: string;
  issuedDate: string;
  expiryDate: string;
  status: string;
  documentPath: string;
  terms: string;
}