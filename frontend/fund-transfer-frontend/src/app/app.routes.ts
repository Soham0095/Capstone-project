import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { TransactionHistory } from './components/transaction-history/transaction-history';

export const routes: Routes = [
	{ path: '', component: LoginComponent },
	{ path: 'dashboard', component: DashboardComponent },
	{ path: 'transaction-history', component: TransactionHistory }
];
