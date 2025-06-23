import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, ReactiveFormsModule, FormsModule,HttpClientModule]
})
export class LoginComponent {
  loginForm: FormGroup;
  mensajeRespuesta: string = '';
  rol: string = '';
  id: number | null = null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get correo() {
    return this.loginForm.get('correo');
  }

  get contrasena() {
    return this.loginForm.get('contrasena');
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const data = this.loginForm.value;

    this.http.post<any>('http://localhost:8080/api/auth/login', data).subscribe({
      next: (response) => {
        this.mensajeRespuesta = response.mensaje;
        this.rol = response.rol;
        this.id = response.id;

        localStorage.setItem('rol', this.rol);
        if (this.id !== null) {
          localStorage.setItem('id', this.id.toString());
        }

        switch (this.rol) {
          case 'PACIENTE':
            this.router.navigate(['dashboards/dashboard-paciente/inicio']);
            break;
          case 'MEDICO':
            this.router.navigate(['dashboards/dashboard-medico/inicio']);
            break;
          case 'ADMIN':
            this.router.navigate(['dashboards/dashboard-admin/inicio']);
            break;
          default:
            console.warn('Rol no reconocido:', this.rol);
        }
      },
      error: (error) => {
        this.mensajeRespuesta = '❌ Error al iniciar sesión';
        console.error(error);
      }
    });
  }
}