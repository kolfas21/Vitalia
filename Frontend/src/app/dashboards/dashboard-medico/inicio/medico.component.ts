import { Component } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, NgModel } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {ConsultasMedicoComponent} from '../../../components/citas-medico/citas-medico';
import { config } from '../../../../config';

@Component({
  selector: 'app-medico',
  standalone: true,
  imports: [CommonModule, FormsModule, NgIf, HttpClientModule, ConsultasMedicoComponent],

  templateUrl: './medico.component.html',
  styleUrls: ['./medico.component.css']
})
export class MedicoComponent implements OnInit {
  seccion: string = 'inicio';
  showUserMenu: boolean = false;
  modoEditar: boolean = false;
  mensaje: string = '';

  // Datos del usuario
  id = localStorage.getItem('id');
  nombre = localStorage.getItem('nombre') || '';
  correo = localStorage.getItem('correo') || '';
  cedula = localStorage.getItem('cedula') || '';
  fechaNacimiento = localStorage.getItem('fechaNacimiento') || '';
  rol = localStorage.getItem('rol') || '';
  token = localStorage.getItem('token') || '';

  // Para edición
  editNombre = this.nombre;
  editCorreo = this.correo;
  editCedula = this.cedula;
  editFechaNacimiento = this.fechaNacimiento;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {}

  toggleUserMenu() {
    this.showUserMenu = !this.showUserMenu;
  }

  activarModoEdicion() {
    this.modoEditar = true;
  }

  guardarCambios() {
    if (!this.id) return;

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    });

    const updatedUser = {
      nombre: this.editNombre,
      correo: this.editCorreo,
      cedula: this.editCedula,
      fechaNacimiento: this.editFechaNacimiento,
      contrasena: '', // opcional
      rol: this.rol
    };

    this.http.put(`${config.apiUrl}/api/usuarios/actualizar/${this.id}`, updatedUser, { headers })
      .subscribe({
        next: (res: any) => {
          this.nombre = this.editNombre;
          this.correo = this.editCorreo;
          this.cedula = this.editCedula;
          this.fechaNacimiento = this.editFechaNacimiento;
          this.modoEditar = false;
          this.mensaje = '✅ Información actualizada con éxito';
        },
        error: (err) => {
          console.error(err);
          this.mensaje = '❌ Error al actualizar';
        }
      });
  }

  cerrarSesion() {
    localStorage.clear();
    location.reload(); // o redirigir a login
  }
}