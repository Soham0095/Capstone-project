import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import { MessageService } from 'primeng/api';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import Aura from '@primeng/themes/aura';
import { jwtInterceptor } from './interceptors/jwt.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimationsAsync(),
    provideBrowserGlobalErrorListeners(),
    // Register the JWT interceptor — it runs on EVERY outgoing HTTP request
    provideHttpClient(withFetch(), withInterceptors([jwtInterceptor])),
    provideRouter(routes),
    provideClientHydration(withEventReplay()),
    providePrimeNG({ theme: { preset: Aura } }),
    MessageService,
  ]
};
