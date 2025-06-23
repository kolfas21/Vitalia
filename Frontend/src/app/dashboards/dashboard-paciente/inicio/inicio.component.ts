import { Component } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {CitasComponent} from '../../../components/citas/citas.component';

@Component({
  selector: 'app-inicio',
  imports: [CommonModule, FormsModule, NgIf, NgFor, CitasComponent],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class PacienteInicioComponent {

    seccion: string = 'cita';

  nuevaCita = {
    fecha: '',
    especialidad: '',
    nota: ''
  };

  diagnosticos = [
    { descripcion: 'Dolor de cabeza - Paracetamol 500mg', fecha: '2025-06-10', medico: 'Dr. Rivera' },
    { descripcion: 'Bronquitis leve - Reposo y jarabe expectorante', fecha: '2025-06-12', medico: 'Dra. Molina' }
  ];

  autorizaciones = [
    { servicio: 'Resonancia magnética', estado: 'Autorizado' },
    { servicio: 'Consulta con neumólogo', estado: 'Pendiente' },
    { servicio: 'Terapia física', estado: 'Rechazado' }
  ];

  solicitarCita() {
    alert('📅 Cita solicitada con éxito');
    this.nuevaCita = { fecha: '', especialidad: '', nota: '' };
  }

}
