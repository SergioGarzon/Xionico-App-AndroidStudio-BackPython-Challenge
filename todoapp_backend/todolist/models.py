from django.db import models

class Task(models.Model):
    # Opciones para el estado de la tarea
    class Status(models.TextChoices):
        PENDING = 'Pending', 'Pendiente'
        COMPLETED = 'completed', 'Completada'

    title = models.CharField(max_length=200, verbose_name="Título")
    description = models.TextField(blank=True, null=True, verbose_name="Descripción")
    status = models.CharField(
        max_length=10,
        choices=Status.choices,
        default=Status.PENDING,
        verbose_name="Estado"
    )