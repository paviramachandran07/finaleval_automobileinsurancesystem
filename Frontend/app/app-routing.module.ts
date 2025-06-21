import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { UserdashboardComponent } from './userdashboard/userdashboard.component';
import { ProposalformComponent } from './proposalform/proposalform.component';
import { PaymentComponent } from './payment/payment.component';
import { OfficerdashboardComponent } from './officerdashboard/officerdashboard.component';
import { ProposalreviewComponent } from './proposalreview/proposalreview.component';
import { PolicyComponent } from './policy/policy.component';

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'userdashboard', component: UserdashboardComponent },
  { path: 'proposalform', component: ProposalformComponent },
  { path: 'payment/:quoteId', component: PaymentComponent },
  { path: 'officerdashboard', component: OfficerdashboardComponent },
  { path: 'proposalreview', component: ProposalreviewComponent },
  { path: 'policy/:policyId', component: PolicyComponent }, 
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }


