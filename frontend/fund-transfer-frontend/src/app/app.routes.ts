import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TransactionHistory } from './components/transaction-history/transaction-history';
import { ProfileComponent } from './components/profile/profile.component';
import { SignupComponent } from './components/signup/signup.component';
import { Transfer } from './components/transfer/transfer';
import { AdminAiComponent } from './components/admin-ai/admin-ai.component';

import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
	{ path: '', component: LoginComponent, canActivate: [authGuard] },
	{ path: 'signup', component: SignupComponent, canActivate: [authGuard] },
	{ path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
	{ path: 'transaction-history', component: TransactionHistory, canActivate: [authGuard] },
	{ path: 'transfer', component: Transfer, canActivate: [authGuard] },
	{ path: 'admin-ai', component: AdminAiComponent, canActivate: [authGuard] },
	{ path: 'profile', component: ProfileComponent, canActivate: [authGuard] }
];
