from django.shortcuts import get_object_or_404
from ninja import Router
from typing import List
from .models import Task
from .schemas import CreateTaskSchema, StatusSchema, TaskSchema

router = Router()

@router.get("/", response=List[TaskSchema])
def list_tasks(request):
    return Task.objects.values()

@router.post("/")
def create_task(request, payload: CreateTaskSchema):
    task = Task.objects.create(title=payload.title, description=payload.description)
    return { "id": task.id }

@router.patch("/{id}", response=TaskSchema)
def update_task(request, id: int, payload: StatusSchema):
    task = get_object_or_404(Task, pk=id)
    task.status = payload.status
    task.save()
    return task