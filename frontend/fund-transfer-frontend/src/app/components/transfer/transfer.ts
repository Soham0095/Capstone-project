import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { GoBackDirective } from '../../directives/go-back-directive';
import { TransferService } from '../services/transfer.service';


@Component({
  selector: 'app-transfer',
  imports: [RouterOutlet, GoBackDirective],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css',
})
export class Transfer {

  transferService = inject(TransferService);


}
