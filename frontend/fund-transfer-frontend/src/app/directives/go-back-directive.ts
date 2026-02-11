import { Directive, HostListener } from '@angular/core';
import { Router } from '@angular/router';

@Directive({
  selector: '[appGoBackDirective]',
})
export class GoBackDirective {
  constructor(private router: Router) { }

  @HostListener('click') onClick() {
    this.router.navigate(['/dashboard']);
  }

}
