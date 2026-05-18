#!/bin/bash

PROMPT="
You are a senior storage engine reviewer.
Do not generate large implementations.
Focus on critique, correctness, durability, concurrency, IO efficiency, and GC pressure.
"

git diff | gemini "$PROMPT"