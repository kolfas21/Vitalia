import { Component, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-consulta-medica',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './citas.component.html',
})
export class CitasMedicaComponent {
  constructor(private http: HttpClient) {
    this.cargarMedicos();
  }

  // Señales reactivas
  medicos = signal<any[]>([]);
  especialidades = computed(() =>
    [...new Set(this.medicos().map(m => m.especialidad))]
  );
  especialidadSeleccionada = signal('');
  medicosFiltrados = computed(() =>
    this.medicos().filter(m => m.especialidad === this.especialidadSeleccionada())
  );
  medicoSeleccionado = signal<any | null>(null);

  fechaSeleccionada = signal<string>('');
  horaSeleccionada = signal<string>('');
  estadoConsulta = signal<string>('');

  horasDisponibles = ['09:00', '10:00', '11:00', '14:00', '15:00', '16:00'];

  cargarMedicos() {
    this.http.get<any[]>('http://localhost:8080/api/medicos').subscribe({
      next: data => this.medicos.set(data),
      error: err => console.error('Error cargando médicos:', err),
    });
  }

  solicitarConsulta() {
    const medico = this.medicoSeleccionado();
    const fecha = this.fechaSeleccionada();
    const hora = this.horaSeleccionada();
    const idUsuario = Number(localStorage.getItem('id'));

    if (!fecha || !hora || !medico) {
      this.estadoConsulta.set('⚠️ Por favor, seleccione médico, fecha y hora.');
      return;
    }

    const fechaHora = `${fecha}T${hora}:00`;

    const body = {
      idUsuario: idUsuario,
      idMedico: medico.id, // ✅ ID del usuario asociado al médico
      fechaConsulta: fechaHora,
    };

    console.log('Consulta enviada:', body);

    this.http.post('http://localhost:8080/api/consulta', body).subscribe({
      next: (resp: any) => {
        this.estadoConsulta.set(
          `✅ Consulta registrada: ${resp.nombrePaciente} con ${resp.nombreMedico} el ${new Date(resp.fechaConsulta).toLocaleString()}`
        );
      },
      error: err => {
        console.error(err);
        this.estadoConsulta.set('❌ Error al registrar la consulta.');
      }
    });
  }
}