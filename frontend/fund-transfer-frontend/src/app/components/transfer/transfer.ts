import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { GoBackDirective } from '../../directives/go-back-directive';
@Component({
  selector: 'app-transfer',
  imports: [RouterOutlet, GoBackDirective],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css',
})
export class Transfer {
}
