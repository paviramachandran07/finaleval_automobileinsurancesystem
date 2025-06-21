export interface Payment {
  paymentId: number;
  quoteId: number;
  paymentDate: string;
  amountPaid: number;
  status: string;
}