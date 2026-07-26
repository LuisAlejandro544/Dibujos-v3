---
name: "ai-drawing-judge"
description: >-
  MANDATORY for AI Drawing Judge integration in 1v1 multiplayer mode. Uses Gemini multimodal vision, structured reasoning, and child-friendly AI prompts for kids' drawing duels.
---

# AI Drawing Judge & Prompt Generator Skill

This skill defines the integration pattern for the **Juez Mágico AI** (AI Drawing Judge) in the KidsDraw Canvas multiplayer 1v1 mode.

## Core Capabilities

1. **AI Drawing Challenge Generator**:
   - Uses Gemini API (`gemini-3.5-flash` or `gemini-3.1-flash-lite-preview`) to dynamically generate creative, fun, age-appropriate drawing prompts for kids (e.g., "Un pulpo chef cocinando pizza en el espacio 🐙🍕").

2. **Multimodal AI Judge & Vision Evaluator**:
   - Takes compressed JPEG/PNG Base64 representations of Player 1's and Player 2's artwork.
   - Applies a cheerful, enthusiastic system prompt with high reasoning to analyze artistic effort, color usage, creativity, and adherence to the prompt.
   - Evaluates both drawings fairly without strict negative criticism, assigning positive star ratings (1 to 5) and child-friendly commentary for both players.

3. **Audio & Voice Integration**:
   - The verdict and feedbacks are automatically spoken aloud via `VoiceAssistant` so non-reading children can listen to the AI Judge's evaluation.
