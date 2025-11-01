from ninja import NinjaAPI
from todolist.api import router as task_router

api = NinjaAPI(title="Mi API de Tareas", version="1.0")

api.add_router('/tasks/', task_router)