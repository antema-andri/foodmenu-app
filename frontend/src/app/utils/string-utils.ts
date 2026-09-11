export function getInitials(fullName: string): string {
  if (!fullName) {
    return '';
  }

  return fullName
    .trim()
    .split(/\s+/)          // découpe par espaces
    .map(word => word[0])  // première lettre de chaque mot
    .join('')
    .toUpperCase();
}

// pour les deux premieres lettres seulement
export function getInitials2(fullName: string): string {
  if (!fullName) {
    return '';
  }

  return fullName
    .trim()
    .split(/\s+/)
    .slice(0, 2)
    .map(word => word[0])
    .join('')
    .toUpperCase();
}