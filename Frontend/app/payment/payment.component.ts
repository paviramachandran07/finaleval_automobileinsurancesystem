import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Payment } from '../models/payment.model';
import { Quote } from '../models/quote.model';
import { PaymentService } from '../payment.service';
import { QuoteService } from '../quote.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html'
})
export class PaymentComponent implements OnInit {
  quote: Quote | null = null;
  payment: Payment = {
    paymentId: 0,
    quoteId: 0,
    paymentDate: new Date().toISOString(),
    amountPaid: 0,
    status: 'Completed'
  };
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private quoteService: QuoteService,
    private paymentService: PaymentService
  ) {}

  ngOnInit(): void {
    const quoteId = +this.route.snapshot.paramMap.get('quoteId')!;
    this.payment.quoteId = quoteId;
    this.quoteService.getQuoteById(quoteId).subscribe({
      next: (quote) => {
        this.quote = quote;
        this.payment.amountPaid = quote.premiumAmount;
      },
      error: (err) => {
        this.errorMessage = 'Error fetching quote: ' + err.message;
        alert(this.errorMessage);
        console.error('Error fetching quote:', err);
      }
    });
  }

  makePayment(): void {
    if (this.quote) {
      this.paymentService.createPayment(this.payment).subscribe({
        next: (payment) => {
          alert('Payment successful!');
          this.router.navigate([`/policy/${payment.paymentId}`]);
        },
        error: (err) => {
          this.errorMessage = 'Error making payment: ' + err.message;
          alert(this.errorMessage);
          console.error('Error making payment:', err);
        }
      });
    } else {
      this.errorMessage = 'No quote available for payment';
      alert(this.errorMessage);
    }
  }
}







     



