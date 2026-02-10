import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TransactionHistory } from './components/transaction-history/transaction-history';
import { TransferComponent } from './components/transfer/transfer.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SignupComponent } from './components/signup/signup.component';

export const routes: Routes = [
	{ path: '', component: LoginComponent },
	{ path: 'signup', component: SignupComponent },
	{ path: 'dashboard', component: DashboardComponent },
	{ path: 'transaction-history', component: TransactionHistory },
	{ path: 'transfer', component: TransferComponent },
	{ path: 'profile', component: ProfileComponent }
];
