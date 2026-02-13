import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountStore } from '../services/account-store.service';
import { GoBackDirective } from '../../directives/go-back-directive';
import { Footer } from '../footer/footer';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, GoBackDirective, Footer],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  accountStore = inject(AccountStore);
}
