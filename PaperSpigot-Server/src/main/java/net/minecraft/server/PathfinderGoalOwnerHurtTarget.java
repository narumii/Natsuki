package net.minecraft.server;

public class PathfinderGoalOwnerHurtTarget extends PathfinderGoalTarget {

  EntityTameableAnimal a;
  EntityLiving b;
  private int c;

  public PathfinderGoalOwnerHurtTarget(EntityTameableAnimal entitytameableanimal) {
    super(entitytameableanimal, false);
    this.a = entitytameableanimal;
    this.a(1);
  }

  public boolean a() {
    if (!this.a.isTamed()) {
      return false;
    } else {
      EntityLiving entityliving = this.a.getOwner();

      if (entityliving == null) {
        return false;
      } else {
        this.b = entityliving.bf();
        int i = entityliving.bg();

        return i != this.c && this.a(this.b, false) && this.a.a(this.b, entityliving);
      }
    }
  }

  public void c() {
    this.e.setGoalTarget(this.b,
        org.bukkit.event.entity.EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET,
        true); // CraftBukkit - reason
    EntityLiving entityliving = this.a.getOwner();

    if (entityliving != null) {
      this.c = entityliving.bg();
    }

    super.c();
  }
}
