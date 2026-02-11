import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TransactionHistory } from './components/transaction-history/transaction-history';
import { ProfileComponent } from './components/profile/profile.component';
import { SignupComponent } from './components/signup/signup.component';
import { Transfer } from './components/transfer/transfer';
import { AdminAiComponent } from './components/admin-ai/admin-ai.component';

export const routes: Routes = [
	{ path: '', component: LoginComponent },
	{ path: 'signup', component: SignupComponent },
	{ path: 'dashboard', component: DashboardComponent },
	{ path: 'transaction-history', component: TransactionHistory },
	{ path: 'transfer', component: Transfer },
	{ path: 'admin-ai', component: AdminAiComponent },
	{ path: 'profile', component: ProfileComponent }
];
