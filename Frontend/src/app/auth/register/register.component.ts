import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
backendErrors: { [key: string]: string } = {};
  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      cedula: ['', [Validators.required, Validators.pattern(/^[0-9]{5,15}$/)]],
      correo: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]],
      repetirContrasena: ['', Validators.required],
      fechaNacimiento: ['', Validators.required],
      genero: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('contrasena')?.value;
    const confirm = form.get('repetirContrasena')?.value;
    return password === confirm ? null : { mismatch: true };
  }

onSubmit(): void {
  this.backendErrors = {}; // Limpiar errores previos

  if (this.registerForm.invalid) {
    this.registerForm.markAllAsTouched();
    return;
  }

  const formData = this.registerForm.value;
  const dataToSend = {
    nombre: formData.nombre,
    cedula: formData.cedula,
    correo: formData.correo,
    contrasena: formData.contrasena,
    fechaNacimiento: formData.fechaNacimiento,
    genero: formData.genero
  };

  this.http.post('http://localhost:8080/api/usuarios/registrar', dataToSend).subscribe({
    next: res => {
      alert('¡Registro exitoso!');
      this.registerForm.reset();
    },
error: err => {
  if (err.status === 400 && err.error?.error) {
    const mensaje = err.error.error.toLowerCase();

    if (mensaje.includes("correo")) {
      this.backendErrors['correo'] = err.error.error;
    } else if (mensaje.includes("cédula") || mensaje.includes("cedula")) {
      this.backendErrors['cedula'] = err.error.error;
    } else {
      this.backendErrors['general'] = err.error.error;
    }
  } else {
    this.backendErrors['general'] = 'Error al registrar. Intenta nuevamente.';
  }
}
  });
}

  // <-- Este era el que faltaba
  onCancel(): void {
    this.registerForm.reset();
  }
}