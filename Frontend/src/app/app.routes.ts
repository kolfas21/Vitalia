import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { PacienteInicioComponent } from './dashboards/dashboard-paciente/inicio/inicio.component';
import { MedicoComponent } from './dashboards/dashboard-medico/inicio/medico.component';
import { InicioComponent } from './dashboards/dashboard-admin/inicio/inicio.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/register', component: RegisterComponent },
  { path: 'dashboards/dashboard-paciente/inicio', component: PacienteInicioComponent },
  { path: 'dashboards/dashboard-medico/inicio', component: MedicoComponent },
  { path: 'dashboards/dashboard-admin/inicio', component: InicioComponent },
  // otras rutas...
  { path: '**', redirectTo: '' }
];