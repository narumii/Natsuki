package net.minecraft.server;

public class PathfinderGoalOwnerHurtByTarget extends PathfinderGoalTarget {

  EntityTameableAnimal a;
  EntityLiving b;
  private int c;

  public PathfinderGoalOwnerHurtByTarget(EntityTameableAnimal entitytameableanimal) {
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
        this.b = entityliving.getLastDamager();
        int i = entityliving.be();

        return i != this.c && this.a(this.b, false) && this.a.a(this.b, entityliving);
      }
    }
  }

  public void c() {
    this.e.setGoalTarget(this.b,
        org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER,
        true); // CraftBukkit - reason
    EntityLiving entityliving = this.a.getOwner();

    if (entityliving != null) {
      this.c = entityliving.be();
    }

    super.c();
  }
}
