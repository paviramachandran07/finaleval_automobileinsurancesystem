import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Proposal } from '../models/proposal.model';
import { ProposalService } from '../proposal.service';
import { Policy } from '../models/policy.model';
import { PolicyService } from '../policy.service';

@Component({
  selector: 'app-officerdashboard',
  templateUrl: './officerdashboard.component.html'
})
export class OfficerdashboardComponent implements OnInit {
  proposals: Proposal[] = [];
  filteredProposals: Proposal[] = [];
  selectedProposal: Proposal | null = null;

  policies: Policy[] = [];
  filteredPolicies: Policy[] = [];

  proposalFilter: string = '';
  policyStatusFilter: string = 'All';
  errorMessage: string = '';

  constructor(
    private proposalService: ProposalService,
    private policyService: PolicyService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProposals();
    this.loadPolicies();
  }

  loadProposals(): void {
    this.proposalService.getAllProposals().subscribe({
      next: (proposals) => {
        this.proposals = proposals.filter(p => p.status === 'Submitted');
        this.filteredProposals = [...this.proposals];
        this.errorMessage = this.proposals.length === 0 ? 'No pending proposals.' : '';
      },
      error: (err) => {
        this.errorMessage = 'Error loading proposals: ' + err.message;
      }
    });
  }

  loadPolicies(): void {
    this.policyService.getAllPolicies().subscribe({
      next: (policies) => {
        this.policies = policies;
        this.applyPolicyFilter();
        this.errorMessage = this.policies.length === 0 ? 'No policies found.' : '';
      },
      error: (err) => {
        this.errorMessage = 'Error loading policies: ' + err.message;
      }
    });
  }

  applyProposalFilter(): void {
    const filter = this.proposalFilter.trim().toLowerCase();
    if (!filter) {
      this.filteredProposals = [...this.proposals];
    } else {
      this.filteredProposals = this.proposals.filter(p =>
        p.userId.toString().includes(filter) || p.vehicleId.toString().includes(filter)
      );
    }
  }

  applyPolicyFilter(): void {
    if (this.policyStatusFilter === 'All') {
      this.filteredPolicies = [...this.policies];
    } else {
      this.filteredPolicies = this.policies.filter(p => p.status === this.policyStatusFilter);
    }
  }

  selectProposal(proposal: Proposal): void {
    this.selectedProposal = proposal;
  }

  onReviewCompleted(): void {
    this.loadProposals(); 
    this.selectedProposal = null;
  }

  togglePolicyStatus(policy: Policy): void {
    const newStatus = policy.status === 'Active' ? 'Cancelled' : 'Active';
    const updatedPolicy: Policy = { ...policy, status: newStatus };
    this.policyService.updatePolicy(policy.policyId, updatedPolicy).subscribe({
      next: () => {
        this.loadPolicies();
        alert(`Policy ${newStatus.toLowerCase()}d successfully!`);
      },
      error: (err) => {
        this.errorMessage = 'Failed to update policy: ' + err.message;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/login']);
  }
}





